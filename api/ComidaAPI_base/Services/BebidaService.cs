using ComidaAPI.Dtos;
using ComidaAPI.Models;
using ComidaAPI.Repositories;

namespace ComidaAPI.Services;

public class BebidaService(IComidaRepository repo)
{
    public Task<List<Bebida>> ObtenerAsync(string userId) => repo.ObtenerBebidasAsync(userId);

    public Task<Bebida> GuardarAsync(string userId, BebidaRequest request)
    {
        Validar(request.Nombre, request.Precio);

        var bebida = new Bebida
        {
            Id = request.Id ?? string.Empty,
            Nombre = request.Nombre.Trim(),
            Precio = request.Precio,
            Lugar = request.Lugar.Trim()
        };

        return repo.GuardarBebidaAsync(userId, bebida);
    }

    public Task EliminarAsync(string userId, string id)
    {
        if (string.IsNullOrWhiteSpace(id))
            throw new ArgumentException("El id de la bebida es obligatorio.");

        return repo.EliminarBebidaAsync(userId, id);
    }

    private static void Validar(string nombre, float precio)
    {
        if (string.IsNullOrWhiteSpace(nombre))
            throw new ArgumentException("El nombre no puede estar vacío.");
        if (precio <= 0)
            throw new ArgumentException("El precio debe ser mayor a 0.");
        if (precio > 9999)
            throw new ArgumentException("El precio no puede superar $9,999 pesos.");
    }
}
