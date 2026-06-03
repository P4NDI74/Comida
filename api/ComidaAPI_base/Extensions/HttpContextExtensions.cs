using System.Security.Claims;

namespace ComidaAPI.Extensions;

public static class HttpContextExtensions
{
    public static string GetFirebaseUserId(this HttpContext context)
    {
        var uid = context.User.FindFirstValue(ClaimTypes.NameIdentifier)
                  ?? context.User.FindFirstValue("firebase_uid");

        if (string.IsNullOrWhiteSpace(uid))
            throw new UnauthorizedAccessException("Token inválido o usuario no autenticado.");

        return uid;
    }
}
