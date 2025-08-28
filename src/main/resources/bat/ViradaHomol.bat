@echo off
echo Consultando tarefas agendadas no servidor 10.192.0.90...
powershell -Command "schtasks /s 10.192.0.90 /query /tn '\BOMFUTURO\' /FO CSV | ConvertFrom-Csv | Where-Object { $_.TaskName -like '*\COPIA_HOMOLOGACAO' } | ForEach-Object { "$($_.TaskName),$($_.'Next Run Time'),$($_.Status)" } | Set-Content "S:\Portal_Virada_Bases\demo\src\main\resources\data\homologacao_atualizacao.csv" -NoTypeInformation"
timeout /t 5