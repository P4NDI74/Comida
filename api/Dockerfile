FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

COPY ["ComidaAPI_base/ComidaAPI.csproj", "ComidaAPI_base/"]
RUN dotnet restore "ComidaAPI_base/ComidaAPI.csproj"

COPY . .
WORKDIR "/src/ComidaAPI_base"
RUN dotnet publish "ComidaAPI.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app

COPY --from=build /app/publish .

ENV ASPNETCORE_URLS=http://0.0.0.0:8080

ENTRYPOINT ["dotnet", "ComidaAPI.dll"]