using ComidaAPI.Models;

namespace ComidaAPI.Repositories;

public interface IComidaRepository
{
    Task<List<Alimento>> ObtenerAlimentosAsync(string userId);
    Task<Alimento> GuardarAlimentoAsync(string userId, Alimento alimento);
    Task EliminarAlimentoAsync(string userId, string id);

    Task<List<Bebida>> ObtenerBebidasAsync(string userId);
    Task<Bebida> GuardarBebidaAsync(string userId, Bebida bebida);
    Task EliminarBebidaAsync(string userId, string id);

    Task<List<Gasto>> ObtenerGastosAsync(string userId);
    Task<Gasto> GuardarGastoAsync(string userId, Gasto gasto);
    Task EliminarTodosGastosAsync(string userId);
}
