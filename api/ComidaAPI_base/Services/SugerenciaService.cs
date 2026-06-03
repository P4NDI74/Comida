using ComidaAPI.Dtos;
using ComidaAPI.Models;
using ComidaAPI.Repositories;

namespace ComidaAPI.Services;

public class SugerenciaService(IComidaRepository repo)
{
    public async Task<Sugerencia> GenerarAsync(string userId, SugerenciaRequest request)
    {
        if (request.Presupuesto <= 0)
            throw new ArgumentException("El presupuesto debe ser mayor a 0.");
        if (!request.IncluirComida && !request.IncluirBebida)
            throw new ArgumentException("Debes incluir comida, bebida o ambas opciones.");

        var alimentos = request.IncluirComida
            ? (await repo.ObtenerAlimentosAsync(userId)).Where(a => a.Precio <= request.Presupuesto).ToList()
            : [];

        var bebidas = request.IncluirBebida
            ? (await repo.ObtenerBebidasAsync(userId)).Where(b => b.Precio <= request.Presupuesto).ToList()
            : [];

        var conjuntos = new List<ConjuntoSugerencia>();

        if (request.IncluirComida && request.IncluirBebida)
        {
            foreach (var alimento in alimentos)
            {
                foreach (var bebida in bebidas)
                {
                    var total = alimento.Precio + bebida.Precio;
                    if (total <= request.Presupuesto)
                    {
                        conjuntos.Add(new ConjuntoSugerencia
                        {
                            Alimento = alimento,
                            Bebida = bebida,
                            CostoTotal = total
                        });
                    }
                }
            }
        }
        else if (request.IncluirComida)
        {
            conjuntos.AddRange(alimentos.Select(a => new ConjuntoSugerencia
            {
                Alimento = a,
                CostoTotal = a.Precio
            }));
        }
        else
        {
            conjuntos.AddRange(bebidas.Select(b => new ConjuntoSugerencia
            {
                Bebida = b,
                CostoTotal = b.Precio
            }));
        }

        conjuntos = conjuntos.OrderBy(c => c.CostoTotal).ToList();

        return new Sugerencia
        {
            Conjuntos = conjuntos,
            PresupuestoRestante = conjuntos.Count == 0
                ? request.Presupuesto
                : request.Presupuesto - conjuntos.First().CostoTotal
        };
    }
}
