package edu.ucsb.cs156.example.entities;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "personal_schedules")
public class PersonalSchedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // This establishes that many PersonalSchedules can belong to one user
  // Only the user_id is stored in the table, and through it we
  // can access the user's details

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  private String name;
  private String description;
  private String quarterYYYYQ;
}