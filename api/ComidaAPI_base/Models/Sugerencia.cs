namespace ComidaAPI.Models;

public class ConjuntoSugerencia
{
    public Alimento? Alimento { get; set; }
    public Bebida? Bebida { get; set; }
    public float CostoTotal { get; set; }
}

public class Sugerencia
{
    public List<ConjuntoSugerencia> Conjuntos { get; set; } = [];
    public float PresupuestoRestante { get; set; }
}
