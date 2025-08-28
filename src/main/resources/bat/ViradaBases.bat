@echo off
echo Consultando tarefas de cópia agendadas no servidor 10.192.0.90...

:: Limpar arquivo existente
if exist "S:\Portal_Virada_Bases\demo\src\main\resources\bat\viradaBase.csv" del "S:\Portal_Virada_Bases\demo\src\main\resources\bat\viradaBase.csv"

:: Executar consulta filtrada
powershell -Command "$tasks = schtasks /s 10.192.0.90 /query /tn '\BOMFUTURO\' /FO CSV | ConvertFrom-Csv | Where-Object {$_.TaskName -like '*\COPIA*'}; $tasks | Select-Object @{Name='Base';Expression={$_.TaskName}}, @{Name='proxima Exec';Expression={if($_.'Next Run Time' -eq 'Não agendado'){'N/A'}else{$_.'Next Run Time'}}} | Export-Csv -Path 'S:\Portal_Virada_Bases\demo\src\main\resources\bat\viradaBase.csv' -NoTypeInformation -Encoding UTF8 -Force"

:: Mostrar resultado formatado
echo Tarefas de Cópia Agendadas:
echo ---------------------------
powershell -Command "Import-Csv 'S:\Portal_Virada_Bases\demo\src\main\resources\bat\viradaBase.csv' | Format-Table -AutoSize"

timeout /t 5
