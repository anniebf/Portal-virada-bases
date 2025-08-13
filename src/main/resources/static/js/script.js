document.querySelectorAll('.table tbody tr').forEach(row => {
  row.addEventListener('click', () => {
    // Remove a classe selected de todas as linhas
    document.querySelectorAll('.table tbody tr.selected').forEach(r => r.classList.remove('selected'));
    // Adiciona a classe selected na linha clicada
    row.classList.add('selected');
  });
});
