namespace ComidaAPI.Dtos;

public class GastoRequest
{
    public string NombreNegocio { get; set; } = string.Empty;
    public string Descripcion { get; set; } = string.Empty;
    public float Costo { get; set; }
}
