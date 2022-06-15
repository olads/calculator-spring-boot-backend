package com.migia.basic.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class History {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String expr;
    private double ans;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User user;

    public History(String expr, double ans){
        this.expr = expr;
        this.ans = ans;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", expr='" + expr + '\'' +
                ", ans=" + ans +
                '}';
    }
    /*  public History createHistory(String expr,double ans, User user)
    {

    }*/
}
