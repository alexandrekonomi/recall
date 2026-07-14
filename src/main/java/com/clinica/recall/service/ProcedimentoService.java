package com.clinica.recall.service;

import com.clinica.recall.domain.entity.Procedimento;
import com.clinica.recall.domain.entity.Usuario;
import com.clinica.recall.dto.request.ProcedimentoRequest;
import com.clinica.recall.dto.response.ProcedimentoResponse;
import com.clinica.recall.repository.ProcedimentoRepository;
import com.clinica.recall.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProcedimentoResponse criar(ProcedimentoRequest request) {
        Usuario medico = usuarioLogado();

        Procedimento procedimento = Procedimento.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .intervaloRetornoDias(request.getIntervaloRetornoDias())
                .templateMensagem(request.getTemplateMensagem())
                .ativo(true)
                .criadoPor(medico)
                .build();

        return toResponse(procedimentoRepository.save(procedimento));
    }

    public List<ProcedimentoResponse> listar() {
        return procedimentoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProcedimentoResponse buscarPorId(Long id) {
        return procedimentoRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
    }

    public ProcedimentoResponse atualizar(Long id, ProcedimentoRequest request) {
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));

        procedimento.setNome(request.getNome());
        procedimento.setDescricao(request.getDescricao());
        procedimento.setIntervaloRetornoDias(request.getIntervaloRetornoDias());
        procedimento.setTemplateMensagem(request.getTemplateMensagem());
        procedimento.setValor(request.getValor());

        return toResponse(procedimentoRepository.save(procedimento));
    }

    public void desativar(Long id) {
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        procedimento.setAtivo(false);
        procedimentoRepository.save(procedimento);
    }

    private Usuario usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private ProcedimentoResponse toResponse(Procedimento p) {
        return ProcedimentoResponse.builder()
                .id(p.getId())
                .nome(p.getNome())
                .descricao(p.getDescricao())
                .intervaloRetornoDias(p.getIntervaloRetornoDias())
                .templateMensagem(p.getTemplateMensagem())
                .valor(p.getValor())
                .ativo(p.isAtivo())
                .build();
    }

    public ProcedimentoResponse toggleStatus(Long id) {
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        procedimento.setAtivo(!procedimento.isAtivo());
        return toResponse(procedimentoRepository.save(procedimento));
    }
}