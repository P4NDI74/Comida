using ComidaAPI.Dtos;
using ComidaAPI.Models;
using ComidaAPI.Repositories;

namespace ComidaAPI.Services;

public class GastoService(IComidaRepository repo)
{
    public Task<List<Gasto>> ObtenerAsync(string userId) => repo.ObtenerGastosAsync(userId);

    public async Task<object> ResumenAsync(string userId)
    {
        var gastos = await repo.ObtenerGastosAsync(userId);
        return new
        {
            gastos,
            totalGastado = gastos.Sum(g => g.Costo)
        };
    }

    public Task<Gasto> GuardarAsync(string userId, GastoRequest request)
    {
        if (string.IsNullOrWhiteSpace(request.NombreNegocio))
            throw new ArgumentException("El nombre del negocio no puede estar vacío.");
        if (request.Costo <= 0)
            throw new ArgumentException("El costo debe ser mayor a 0.");

        var gasto = new Gasto
        {
            NombreNegocio = request.NombreNegocio.Trim(),
            Descripcion = request.Descripcion.Trim(),
            Costo = request.Costo
        };

        return repo.GuardarGastoAsync(userId, gasto);
    }

    public Task EliminarTodosAsync(string userId) => repo.EliminarTodosGastosAsync(userId);
}
