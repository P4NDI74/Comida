using System.Security.Claims;
using FirebaseAdmin.Auth;

namespace ComidaAPI.Middleware;

public class FirebaseAuthMiddleware(RequestDelegate next)
{
    public async Task InvokeAsync(HttpContext context)
    {
        if (!context.Request.Path.StartsWithSegments("/api") ||
            HttpMethods.IsOptions(context.Request.Method))
        {
            await next(context);
            return;
        }

        var header = context.Request.Headers.Authorization.ToString();
        if (string.IsNullOrWhiteSpace(header) || !header.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsJsonAsync(new { error = "Falta el token Bearer de Firebase." });
            return;
        }

        var idToken = header["Bearer ".Length..].Trim();

        try
        {
            var decoded = await FirebaseAuth.DefaultInstance.VerifyIdTokenAsync(idToken);
            var claims = new List<Claim>
            {
                new(ClaimTypes.NameIdentifier, decoded.Uid),
                new("firebase_uid", decoded.Uid)
            };

            if (decoded.Claims.TryGetValue("email", out var email) && email is not null)
            {
                claims.Add(new Claim(ClaimTypes.Email, email.ToString()!));
            }

            var identity = new ClaimsIdentity(claims, "Firebase");
            context.User = new ClaimsPrincipal(identity);

            await next(context);
        }
        catch
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsJsonAsync(new { error = "Token de Firebase inválido o vencido." });
        }
    }
}
