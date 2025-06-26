package com.haiya.entity.pojo;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
       private Double max;
       private Double min;
       private Integer alert_level;

   }
   