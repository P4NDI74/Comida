using FirebaseAdmin;
using Google.Apis.Auth.OAuth2;
using Google.Cloud.Firestore;
using ComidaAPI.Repositories;
using ComidaAPI.Services;

namespace ComidaAPI.Extensions;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddFirebaseServices(
        this IServiceCollection services,
        IConfiguration configuration)
    {
        var credentialsJson = Environment.GetEnvironmentVariable("FIREBASE_CREDENTIALS");

        GoogleCredential credential;

        if (!string.IsNullOrWhiteSpace(credentialsJson))
        {
            // En Render: variable de entorno con el JSON de la service account (en base64)
            var json = System.Text.Encoding.UTF8.GetString(Convert.FromBase64String(credentialsJson));
            credential = GoogleCredential.FromJson(json);
        }
        else
        {
            // Local: usa Application Default Credentials (gcloud auth o GOOGLE_APPLICATION_CREDENTIALS)
            credential = GoogleCredential.GetApplicationDefault();
        }

        FirebaseApp.Create(new AppOptions
        {
            Credential = credential
        });

        var projectId = configuration["Firebase:ProjectId"]
            ?? throw new InvalidOperationException("Falta Firebase:ProjectId en la configuración.");

        services.AddSingleton(_ =>
        {
            var firestoreCredential = credential.IsCreateScopedRequired
                ? credential.CreateScoped("https://www.googleapis.com/auth/datastore")
                : credential;

            return new FirestoreDbBuilder
            {
                ProjectId = projectId,
                Credential = firestoreCredential
            }.Build();
        });

        return services;
    }

    public static IServiceCollection AddApplicationServices(this IServiceCollection services)
    {
        services.AddScoped<IComidaRepository, FirestoreComidaRepository>();
        services.AddScoped<AlimentoService>();
        services.AddScoped<BebidaService>();
        services.AddScoped<GastoService>();
        services.AddScoped<SugerenciaService>();
        return services;
    }
}
