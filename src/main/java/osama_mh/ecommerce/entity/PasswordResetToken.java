package osama_mh.ecommerce.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiration;

    private Boolean isVerified = false;

    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL )
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name ="FK_password_reset_tokens_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private AppUser user;

}
