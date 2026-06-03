using ComidaAPI.Dtos;
using ComidaAPI.Extensions;
using ComidaAPI.Services;
using Microsoft.AspNetCore.Mvc;

namespace ComidaAPI.Controllers;

[ApiController]
[Route("api/gastos")]
public class GastosController(GastoService service) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> Obtener()
    {
        var userId = HttpContext.GetFirebaseUserId();
        return Ok(await service.ResumenAsync(userId));
    }

    [HttpPost]
    public async Task<IActionResult> Guardar([FromBody] GastoRequest request)
    {
        try
        {
            var userId = HttpContext.GetFirebaseUserId();
            var gasto = await service.GuardarAsync(userId, request);
            return Ok(gasto);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
    }

    [HttpDelete]
    public async Task<IActionResult> EliminarTodos()
    {
        var userId = HttpContext.GetFirebaseUserId();
        await service.EliminarTodosAsync(userId);
        return Ok(new { mensaje = "Gastos eliminados correctamente." });
    }
}
