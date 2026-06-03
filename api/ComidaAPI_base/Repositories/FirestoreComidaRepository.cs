using ComidaAPI.Models;
using Google.Cloud.Firestore;

namespace ComidaAPI.Repositories;

public class FirestoreComidaRepository(FirestoreDb db) : IComidaRepository
{
    private CollectionReference Alimentos(string userId) =>
        db.Collection("usuarios").Document(userId).Collection("alimentos");

    private CollectionReference Bebidas(string userId) =>
        db.Collection("usuarios").Document(userId).Collection("bebidas");

    private CollectionReference Gastos(string userId) =>
        db.Collection("usuarios").Document(userId).Collection("gastos");

    public async Task<List<Alimento>> ObtenerAlimentosAsync(string userId)
    {
        var snapshot = await Alimentos(userId).GetSnapshotAsync();
        return snapshot.Documents
            .Select(d => d.ConvertTo<Alimento>())
            .OrderBy(a => a.Nombre)
            .ToList();
    }

    public async Task<Alimento> GuardarAlimentoAsync(string userId, Alimento alimento)
    {
        var doc = string.IsNullOrWhiteSpace(alimento.Id)
            ? Alimentos(userId).Document()
            : Alimentos(userId).Document(alimento.Id);

        alimento.Id = doc.Id;
        alimento.UserId = userId;
        await doc.SetAsync(alimento);
        return alimento;
    }

    public async Task EliminarAlimentoAsync(string userId, string id)
    {
        await Alimentos(userId).Document(id).DeleteAsync();
    }

    public async Task<List<Bebida>> ObtenerBebidasAsync(string userId)
    {
        var snapshot = await Bebidas(userId).GetSnapshotAsync();
        return snapshot.Documents
            .Select(d => d.ConvertTo<Bebida>())
            .OrderBy(b => b.Nombre)
            .ToList();
    }

    public async Task<Bebida> GuardarBebidaAsync(string userId, Bebida bebida)
    {
        var doc = string.IsNullOrWhiteSpace(bebida.Id)
            ? Bebidas(userId).Document()
            : Bebidas(userId).Document(bebida.Id);

        bebida.Id = doc.Id;
        bebida.UserId = userId;
        await doc.SetAsync(bebida);
        return bebida;
    }

    public async Task EliminarBebidaAsync(string userId, string id)
    {
        await Bebidas(userId).Document(id).DeleteAsync();
    }

    public async Task<List<Gasto>> ObtenerGastosAsync(string userId)
    {
        var snapshot = await Gastos(userId).GetSnapshotAsync();
        return snapshot.Documents
            .Select(d => d.ConvertTo<Gasto>())
            .OrderByDescending(g => g.Fecha)
            .ToList();
    }

    public async Task<Gasto> GuardarGastoAsync(string userId, Gasto gasto)
    {
        var doc = Gastos(userId).Document();
        gasto.Id = doc.Id;
        gasto.UserId = userId;
        gasto.Fecha = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
        await doc.SetAsync(gasto);
        return gasto;
    }

    public async Task EliminarTodosGastosAsync(string userId)
    {
        var snapshot = await Gastos(userId).GetSnapshotAsync();

        foreach (var grupo in snapshot.Documents.Chunk(450))
        {
            var batch = db.StartBatch();
            foreach (var doc in grupo)
            {
                batch.Delete(doc.Reference);
            }
            await batch.CommitAsync();
        }
    }
}
