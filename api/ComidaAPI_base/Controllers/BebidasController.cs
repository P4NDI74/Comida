using ComidaAPI.Dtos;
using ComidaAPI.Extensions;
using ComidaAPI.Services;
using Microsoft.AspNetCore.Mvc;

namespace ComidaAPI.Controllers;

[ApiController]
[Route("api/bebidas")]
public class BebidasController(BebidaService service) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> Obtener()
    {
        var userId = HttpContext.GetFirebaseUserId();
        return Ok(await service.ObtenerAsync(userId));
    }

    [HttpPost]
    public async Task<IActionResult> Guardar([FromBody] BebidaRequest request)
    {
        try
        {
            var userId = HttpContext.GetFirebaseUserId();
            var bebida = await service.GuardarAsync(userId, request);
            return Ok(bebida);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Eliminar(string id)
    {
        try
        {
            var userId = HttpContext.GetFirebaseUserId();
            await service.EliminarAsync(userId, id);
            return Ok(new { mensaje = "Bebida eliminada correctamente." });
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
    }
}
