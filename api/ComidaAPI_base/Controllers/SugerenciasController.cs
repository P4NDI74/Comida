using ComidaAPI.Dtos;
using ComidaAPI.Extensions;
using ComidaAPI.Services;
using Microsoft.AspNetCore.Mvc;

namespace ComidaAPI.Controllers;

[ApiController]
[Route("api/sugerencias")]
public class SugerenciasController(SugerenciaService service) : ControllerBase
{
    [HttpPost]
    public async Task<IActionResult> Generar([FromBody] SugerenciaRequest request)
    {
        try
        {
            var userId = HttpContext.GetFirebaseUserId();
            return Ok(await service.GenerarAsync(userId, request));
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
    }
}
