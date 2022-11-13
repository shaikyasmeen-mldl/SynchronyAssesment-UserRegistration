package com.synchrony.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "USERIMAGE")
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @Lob
    @Column(name = "USER_IMAGE", nullable = true, columnDefinition = "mediumblob")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    private String ImgurHashCode;

}
