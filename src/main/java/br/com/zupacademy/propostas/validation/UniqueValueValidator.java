package br.com.zupacademy.propostas.validation;

import br.com.zupacademy.propostas.exception.ErroRegraDeNegocio;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue,Object> {

    private Class<?> classe;
    private String campo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.campo = constraintAnnotation.campo();
        this.classe = constraintAnnotation.classe();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Query sql = entityManager.createQuery("select 1 from "+classe.getName()+" where "+campo+" = :value");
        sql.setParameter("value", value);
        List<?> retorno = sql.getResultList();
        Assert.isTrue(retorno.size() <= 1,"Foi encontrado mais de 1 registro na tabela "+classe.getName()+" com o mesmo "+campo+" = "+value);

        if (!retorno.isEmpty() && classe.getSimpleName().equals("Proposta")){
            throw new ErroRegraDeNegocio("Não pode haver mais de uma proposta para o mesmo documento",campo,value);
        }
        return retorno.isEmpty();
    }
}
