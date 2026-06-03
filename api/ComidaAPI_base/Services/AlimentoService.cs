using ComidaAPI.Dtos;
using ComidaAPI.Models;
using ComidaAPI.Repositories;

namespace ComidaAPI.Services;

public class AlimentoService(IComidaRepository repo)
{
    public Task<List<Alimento>> ObtenerAsync(string userId) => repo.ObtenerAlimentosAsync(userId);

    public Task<Alimento> GuardarAsync(string userId, AlimentoRequest request)
    {
        Validar(request.Nombre, request.Precio);

        var alimento = new Alimento
        {
            Id = request.Id ?? string.Empty,
            Nombre = request.Nombre.Trim(),
            Precio = request.Precio,
            Lugar = request.Lugar.Trim()
        };

        return repo.GuardarAlimentoAsync(userId, alimento);
    }

    public Task EliminarAsync(string userId, string id)
    {
        if (string.IsNullOrWhiteSpace(id))
            throw new ArgumentException("El id del alimento es obligatorio.");

        return repo.EliminarAlimentoAsync(userId, id);
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
