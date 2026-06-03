using ComidaAPI.Dtos;
using ComidaAPI.Extensions;
using ComidaAPI.Services;
using Microsoft.AspNetCore.Mvc;

namespace ComidaAPI.Controllers;

[ApiController]
[Route("api/alimentos")]
public class AlimentosController(AlimentoService service) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> Obtener()
    {
        var userId = HttpContext.GetFirebaseUserId();
        return Ok(await service.ObtenerAsync(userId));
    }

    [HttpPost]
    public async Task<IActionResult> Guardar([FromBody] AlimentoRequest request)
    {
        try
        {
            var userId = HttpContext.GetFirebaseUserId();
            var alimento = await service.GuardarAsync(userId, request);
            return Ok(alimento);
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
            return Ok(new { mensaje = "Alimento eliminado correctamente." });
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
    }
}
