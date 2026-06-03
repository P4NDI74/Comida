using Google.Cloud.Firestore;

namespace ComidaAPI.Models;

[FirestoreData]
public class Bebida
{
    [FirestoreProperty("id")]
    public string Id { get; set; } = string.Empty;

    [FirestoreProperty("userId")]
    public string UserId { get; set; } = string.Empty;

    [FirestoreProperty("nombre")]
    public string Nombre { get; set; } = string.Empty;

    [FirestoreProperty("precio")]
    public float Precio { get; set; }

    [FirestoreProperty("lugar")]
    public string Lugar { get; set; } = string.Empty;
}
