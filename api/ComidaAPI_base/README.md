# ComidaAPI

API en C# / ASP.NET Core para la app Android Jetpack Compose `Comida`.

Arquitectura:

```text
App Android -> ComidaAPI ASP.NET Core -> Firebase Auth + Cloud Firestore
```

La app Android sigue usando Firebase Auth para iniciar sesión. Después, obtiene el ID token del usuario y lo manda en cada petición:

```http
Authorization: Bearer TOKEN_DE_FIREBASE
```

La API valida ese token con Firebase Admin SDK, obtiene el `uid`, aplica reglas de negocio y lee/escribe en Firestore.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/auth/me` | Validar token y devolver uid/email |
| GET | `/api/alimentos` | Obtener alimentos del usuario |
| POST | `/api/alimentos` | Crear o editar alimento |
| DELETE | `/api/alimentos/{id}` | Eliminar alimento |
| GET | `/api/bebidas` | Obtener bebidas del usuario |
| POST | `/api/bebidas` | Crear o editar bebida |
| DELETE | `/api/bebidas/{id}` | Eliminar bebida |
| GET | `/api/gastos` | Obtener gastos y total gastado |
| POST | `/api/gastos` | Crear gasto |
| DELETE | `/api/gastos` | Eliminar todos los gastos |
| POST | `/api/sugerencias` | Generar sugerencias con presupuesto |

## Paquetes

```bash
dotnet add package FirebaseAdmin --version 3.5.0
dotnet add package Google.Cloud.Firestore --version 4.2.0
dotnet add package Swashbuckle.AspNetCore --version 6.6.2
```

`Microsoft.AspNetCore.Authentication.JwtBearer` es opcional en este ejemplo. Firebase ID Token sí es un JWT, pero aquí se valida con `FirebaseAuth.DefaultInstance.VerifyIdTokenAsync(...)`.

## Configuración local

1. En Firebase Console, abre tu proyecto.
2. Ve a **Configuración del proyecto > Cuentas de servicio**.
3. Genera una llave privada JSON.
4. No subas ese JSON a GitHub.
5. Configura variables de entorno.

PowerShell:

```powershell
$env:FIREBASE_PROJECT_ID="tu-project-id"
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\ruta\serviceAccountKey.json"
dotnet run
```

CMD:

```cmd
set FIREBASE_PROJECT_ID=tu-project-id
set GOOGLE_APPLICATION_CREDENTIALS=C:\ruta\serviceAccountKey.json
dotnet run
```

Linux/macOS:

```bash
export FIREBASE_PROJECT_ID="tu-project-id"
export GOOGLE_APPLICATION_CREDENTIALS="/ruta/serviceAccountKey.json"
dotnet run
```

## Configuración para Render

Variables recomendadas:

```text
FIREBASE_PROJECT_ID=tu-project-id
FIREBASE_CREDENTIALS_JSON={...pega todo el JSON de la cuenta de servicio...}
```

No uses `GOOGLE_APPLICATION_CREDENTIALS` en Render si no vas a subir un archivo; usa mejor `FIREBASE_CREDENTIALS_JSON`.

## Ejemplos JSON

Crear alimento:

```json
{
  "nombre": "Torta",
  "precio": 35,
  "lugar": "Cafetería"
}
```

Editar alimento:

```json
{
  "id": "idDelDocumento",
  "nombre": "Torta de milanesa",
  "precio": 45,
  "lugar": "Cafetería"
}
```

Crear gasto:

```json
{
  "nombreNegocio": "Cafetería",
  "descripcion": "Comida del día",
  "costo": 65
}
```

Generar sugerencias:

```json
{
  "presupuesto": 80,
  "incluirComida": true,
  "incluirBebida": true
}
```

## Prueba rápida con curl

```bash
curl -H "Authorization: Bearer TU_TOKEN" https://localhost:7000/api/alimentos
```

## Cambio necesario en Android

Tus repositorios actuales (`AlimentoRepositorio`, `BebidaRepositorio`, `GastoRepositorio`) usan Firestore directo. El siguiente paso es reemplazarlos por repositorios HTTP con Retrofit/OkHttp, manteniendo Firebase Auth solo para obtener el token.
