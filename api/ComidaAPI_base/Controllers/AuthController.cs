using ComidaAPI.Extensions;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace ComidaAPI.Controllers;

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    [HttpGet("me")]
    public IActionResult Me()
    {
        var uid = HttpContext.GetFirebaseUserId();
        var email = User.FindFirstValue(ClaimTypes.Email);
        return Ok(new { uid, email });
    }
}
