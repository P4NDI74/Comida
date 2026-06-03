using ComidaAPI.Extensions;
using ComidaAPI.Middleware;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

builder.Services.AddEndpointsApiExplorer();

builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1", new() 
    { 
        Title = "ComidaAPI", 
        Version = "v1" 
    });
});

builder.Services.AddCors(options =>
{
    options.AddPolicy("DefaultCors", policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyHeader()
              .AllowAnyMethod();
    });
});

builder.Services.AddFirebaseServices(builder.Configuration);
builder.Services.AddApplicationServices();

var app = builder.Build();

// Swagger activado también en producción para poder verlo en Render
app.UseSwagger();
app.UseSwaggerUI();

app.UseCors("DefaultCors");

app.UseMiddleware<FirebaseAuthMiddleware>();

app.MapGet("/", () => Results.Ok(new
{
    nombre = "ComidaAPI",
    estado = "activa",
    swagger = "/swagger",
    health = "/health"
}));

app.MapGet("/health", () => Results.Ok(new
{
    status = "ok"
}));

app.MapControllers();

// Importante para nube: Render asigna el puerto por variable de entorno.
// No se usa localhost.
var port = Environment.GetEnvironmentVariable("PORT") ?? "8080";
app.Run($"http://0.0.0.0:{port}");