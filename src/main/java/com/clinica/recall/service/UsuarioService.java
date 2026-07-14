package com.clinica.recall.service;

import com.clinica.recall.domain.entity.Usuario;
import com.clinica.recall.domain.enums.RolePerfil;
import com.clinica.recall.dto.request.AtivarContaRequest;
import com.clinica.recall.dto.request.ConvidarUsuarioRequest;
import com.clinica.recall.dto.response.ConviteInfoResponse;
import com.clinica.recall.dto.response.UsuarioResponse;
import com.clinica.recall.repository.ConfiguracaoClinicaRepository;
import com.clinica.recall.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracaoClinicaRepository configuracaoClinicaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioResponse convidar(ConvidarUsuarioRequest request) {
        usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Já existe um usuário com este e-mail");
        });

        Usuario convidadoPor = usuarioLogado();
        String token = UUID.randomUUID().toString();

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .perfil(request.getPerfil())
                .ativo(true)
                .tokenConvite(token)
                .conviteExpiraEm(LocalDateTime.now().plusDays(7))
                .convidadoPorId(convidadoPor.getId())
                .build();

        usuario = usuarioRepository.save(usuario);

        String nomeClinica = configuracaoClinicaRepository.findAll().stream()
                .findFirst()
                .map(c -> c.getNomeClinica())
                .orElse("a clínica");

        emailService.enviarConvite(usuario.getEmail(), usuario.getNome(), nomeClinica, token, request.getPerfil());

        return toResponse(usuario);
    }

    public ConviteInfoResponse buscarConvite(String token) {
        return usuarioRepository.findByTokenConvite(token)
                .map(u -> {
                    boolean valido = u.getSenha() == null
                            && u.getConviteExpiraEm() != null
                            && u.getConviteExpiraEm().isAfter(LocalDateTime.now());

                    String nomeClinica = configuracaoClinicaRepository.findAll().stream()
                            .findFirst()
                            .map(c -> c.getNomeClinica())
                            .orElse("a clínica");

                    return ConviteInfoResponse.builder()
                            .nome(u.getNome())
                            .email(u.getEmail())
                            .perfil(u.getPerfil().name())
                            .nomeClinica(nomeClinica)
                            .valido(valido)
                            .build();
                })
                .orElse(ConviteInfoResponse.builder().valido(false).build());
    }

    public void ativarConta(AtivarContaRequest request) {
        Usuario usuario = usuarioRepository.findByTokenConvite(request.getToken())
                .orElseThrow(() -> new RuntimeException("Convite inválido"));

        if (usuario.getSenha() != null) {
            throw new RuntimeException("Esta conta já foi ativada");
        }

        if (usuario.getConviteExpiraEm() == null || usuario.getConviteExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Este convite expirou. Peça um novo convite ao administrador");
        }

        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTokenConvite(null);
        usuario.setConviteExpiraEm(null);
        usuarioRepository.save(usuario);
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void reativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    private Usuario usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .perfil(u.getPerfil())
                .ativo(u.isAtivo())
                .contaAtivada(u.getSenha() != null)
                .build();
    }
}