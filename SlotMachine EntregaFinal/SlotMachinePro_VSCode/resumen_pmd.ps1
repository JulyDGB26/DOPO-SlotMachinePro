# SlotMachinePro - FASE 3
# Lectura del XML REAL generado por PMD (no altera archivos Java).
param(
    [string]$Reporte = '.\target\pmd.xml'
)

if (-not (Test-Path -LiteralPath $Reporte -PathType Leaf)) {
    Write-Host "ERROR: No se encontro $Reporte. Ejecuta primero: mvn test-compile pmd:pmd" -ForegroundColor Red
    exit 1
}

try {
    [xml]$pmd = Get-Content -LiteralPath $Reporte -Raw -Encoding UTF8
} catch {
    Write-Host "ERROR: No se pudo leer el XML de PMD. $_" -ForegroundColor Red
    exit 1
}

# local-name() admite los espacios de nombres XML usados por PMD 7.
$filas = @()
foreach ($file in $pmd.SelectNodes("//*[local-name()='file']")) {
    $archivo = $file.GetAttribute('name')
    foreach ($v in $file.SelectNodes("*[local-name()='violation']")) {
        $valor = $v.GetAttribute('priority')
        $nivel = 0
        if (-not [int]::TryParse($valor, [ref]$nivel)) { continue }
        $filas += [pscustomobject]@{
            Prioridad = $nivel
            Regla = $v.GetAttribute('rule')
            Archivo = $archivo
            Linea = $v.GetAttribute('beginline')
            Mensaje = ($v.InnerText -replace '\s+', ' ').Trim()
        }
    }
}

Write-Host "`nSLOTMACHINEPRO - RESUMEN PMD REAL" -ForegroundColor Cyan
Write-Host ("Total de incidencias: {0}" -f $filas.Count)
foreach ($nivel in 1..5) {
    $n = @($filas | Where-Object { $_.Prioridad -eq $nivel }).Count
    Write-Host ("  Prioridad {0}: {1}" -f $nivel, $n)
}
$altas = @($filas | Where-Object { $_.Prioridad -le 2 }).Count
Write-Host ("Pendientes prioridad 1 y 2: {0}" -f $altas) -ForegroundColor Yellow

if ($filas.Count -gt 0) {
    Write-Host "`nReglas mas frecuentes:"
    $filas | Group-Object Regla | Sort-Object Count -Descending | Select-Object -First 15 Count, Name | Format-Table -AutoSize
    Write-Host "`nIncidencias de prioridades 1 y 2:"
    $filas | Where-Object { $_.Prioridad -le 2 } | Sort-Object Prioridad, Archivo | Select-Object -First 40 Prioridad, Regla, Archivo, Linea | Format-Table -Wrap
}

$dir = '.\target\diagnostico-pmd'
New-Item -ItemType Directory -Force -Path $dir | Out-Null
$filas | Sort-Object Prioridad, Regla, Archivo | Export-Csv -Path (Join-Path $dir 'incidencias-pmd.csv') -NoTypeInformation -Encoding UTF8
Write-Host "`nDetalle CSV: $dir\incidencias-pmd.csv" -ForegroundColor Green
