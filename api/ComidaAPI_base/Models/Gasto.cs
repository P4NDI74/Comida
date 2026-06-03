using Google.Cloud.Firestore;

namespace ComidaAPI.Models;

[FirestoreData]
public class Gasto
{
    [FirestoreProperty("id")]
    public string Id { get; set; } = string.Empty;

    [FirestoreProperty("userId")]
    public string UserId { get; set; } = string.Empty;

    [FirestoreProperty("nombreNegocio")]
    public string NombreNegocio { get; set; } = string.Empty;

    [FirestoreProperty("descripcion")]
    public string Descripcion { get; set; } = string.Empty;

    [FirestoreProperty("costo")]
    public float Costo { get; set; }

    [FirestoreProperty("fecha")]
    public long Fecha { get; set; } = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
}
