/**
 * Lista todos os usuários disponíveis no sistema.
 *
 * @return Lista de DTOs representando os usuários.
 */
public List<UsuarioResponseDto> listarTodos() {
    return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
}
