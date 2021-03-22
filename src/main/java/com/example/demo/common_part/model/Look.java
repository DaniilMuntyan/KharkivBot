package com.example.demo.common_part.model;

import com.example.demo.common_part.utils.Emoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="look", schema="public")
@Entity
public final class Look {
    @Id
    @SequenceGenerator(name = "LOOK_SEQUENCE", sequenceName = "look_look_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "LOOK_SEQUENCE")
    @Column(name="look_id")
    private Long id;

    @Column(name="flat_id")
    private Integer flatId;

    @Column(name="is_rent_flat")
    private Boolean isRentFlat;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="username")
    private String userName;

    @Column(name="phone")
    private String phone;

    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;

    @Override
    public String toString() {
        String diamond = Emoji.ORANGE_DIAMOND.toString();
        return diamond + " Имя: " +
                (this.firstName == null || this.firstName.isEmpty() ? "отсутствует" : this.firstName) + "\n" +
                diamond + " Фамилия: " +
                (this.lastName == null || this.lastName.isEmpty() ? "отсутствует" : this.lastName) + "\n" +
                diamond + " Телеграм ник: @" +
                (this.userName == null || this.userName.isEmpty() ? "отсутствует" : this.userName) + "\n" +
                diamond + " Телефон: " +
                (this.phone == null || this.phone.isEmpty() ? "отсутствует" : this.phone) + "\n" +
                diamond + " Дата записи: " +
                (this.createdAt == null ? "отсутствует" : this.createdAt);
    }

}
