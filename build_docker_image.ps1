$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
$parentContext = if ($Env:PLATFORM_PARENT_CONTEXT) { $Env:PLATFORM_PARENT_CONTEXT } else { ".." }
if (-not (Test-Path (Join-Path $parentContext "pom.xml"))) {
    throw "Set PLATFORM_PARENT_CONTEXT to the LA Referencia Platform checkout."
}
docker build --build-context "platform-parent=$parentContext" --tag lareferencia/oai-pmh:local .
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
