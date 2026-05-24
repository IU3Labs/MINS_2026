param(
    [string]$DriveLetter = "X",
    [int]$Port = 9090
)

$projectPath = (Resolve-Path -LiteralPath (Split-Path -Parent $MyInvocation.MyCommand.Path)).Path
$drive = "$DriveLetter`:"
$mounted = $false
$exitCode = 1

try {
    if (Test-Path "$drive\") {
        throw "Drive $drive already exists. Choose another drive letter with -DriveLetter."
    }

    & subst $drive $projectPath
    if ($LASTEXITCODE -ne 0 -or -not (Test-Path "$drive\")) {
        throw "Failed to map $projectPath to $drive."
    }
    $mounted = $true

    Push-Location "$drive\"
    $mavenArgs = @(
        "-DskipTests",
        "compile",
        "exec:java",
        "-Dexec.mainClass=org.example.ReferenceServiceMain",
        "-Dreference.port=$Port"
    )
    & mvn @mavenArgs
    $exitCode = $LASTEXITCODE
} finally {
    if ((Get-Location).Path -eq "$drive\") {
        Pop-Location
    }
    if ($mounted) {
        & subst $drive /D
    }
}

exit $exitCode
