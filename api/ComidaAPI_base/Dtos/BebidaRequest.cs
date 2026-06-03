namespace ComidaAPI.Dtos;

public class BebidaRequest
{
    public string? Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public float Precio { get; set; }
    public string Lugar { get; set; } = string.Empty;
}
