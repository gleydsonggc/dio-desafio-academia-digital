package me.dio.academia.digital.service.impl;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.Matricula;
import me.dio.academia.digital.entity.form.MatriculaForm;
import me.dio.academia.digital.repository.AlunoRepository;
import me.dio.academia.digital.repository.MatriculaRepository;
import me.dio.academia.digital.service.IMatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatriculaServiceImpl implements IMatriculaService {

  @Autowired
  private MatriculaRepository matriculaRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Override
  public Matricula create(MatriculaForm form) {
    Matricula matricula = new Matricula();
    Aluno aluno = alunoRepository.findById(form.getAlunoId()).get();

    matricula.setAluno(aluno);

    return matriculaRepository.save(matricula);
  }

  @Override
  public Matricula get(Long id) {
    return matriculaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Matricula com id " + id + " não existe."));
  }

  @Override
  public List<Matricula> getAll(String bairro) {

    if(bairro == null){
      return matriculaRepository.findAll();
    }else{
      return matriculaRepository.findAlunosMatriculadosBairro(bairro);
    }

  }

  @Override
  public Matricula update(Long id, MatriculaForm matriculaForm) {
    return alunoRepository.findById(matriculaForm.getAlunoId())
            .map(aluno -> matriculaRepository
                    .findById(id)
                    .map(matricula -> {
                      matricula.setAluno(aluno);
                      return matricula;
                    }).orElseThrow(() -> new RuntimeException("Matricula com id " + id + " não existe."))
            ).orElseThrow(() -> new RuntimeException("Aluno com id " + id + " não existe."));
  }

  @Override
  public void delete(Long id) {
    matriculaRepository.findById(id)
            .ifPresentOrElse(
                    matriculaRepository::delete,
                    () -> {
                      throw new RuntimeException("Matricula com id " + id + " não existe.");
                    }
            );
  }



}
