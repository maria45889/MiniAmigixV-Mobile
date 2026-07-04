$sdk = 'C:\Users\majo1\AppData\Local\Android\Sdk'
$zip = Join-Path $env:TEMP 'cmdline-tools.zip'
$url = 'https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip'
Write-Output "Downloading $url to $zip"
Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing
$tmp = Join-Path $env:TEMP 'cmdline-tools-extract'
if (Test-Path $tmp) { Remove-Item -Recurse -Force $tmp }
Expand-Archive -LiteralPath $zip -DestinationPath $tmp -Force
# Buscar la carpeta extraída que contenga bin\sdkmanager.bat
$found = Get-ChildItem -Path $tmp -Directory | ForEach-Object {
	if (Test-Path (Join-Path $_.FullName 'bin\sdkmanager.bat')) { $_.FullName }
} | Select-Object -First 1
if (-not $found) {
	$found = Get-ChildItem -Path $tmp -Recurse -Directory | ForEach-Object {
		if (Test-Path (Join-Path $_.FullName 'bin\sdkmanager.bat')) { $_.FullName }
	} | Select-Object -First 1
}
$dest = Join-Path $sdk 'cmdline-tools\\latest'
New-Item -ItemType Directory -Force -Path $dest | Out-Null
if ($found) {
	Copy-Item -Recurse -Force -Path (Join-Path $found '*') -Destination $dest
	Write-Output "Installed to $dest"
	Get-ChildItem -Path $dest | Select-Object -First 40
} else {
	Write-Error "No se encontró la carpeta bin de cmdline-tools en la extracción. Contenido de $tmp:" 
	Get-ChildItem -Path $tmp -Recurse | Select-Object -First 60
	exit 1
}
Remove-Item $zip -Force
