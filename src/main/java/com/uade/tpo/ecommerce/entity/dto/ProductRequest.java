package com.uade.tpo.ecommerce.entity.dto;

import lombok.Data;

@Data
public class ProductRequest {
  private Long id;
  private String title;
  private String description;
  private Double price;
  private Float discount;
  private String stock;
  private String image_url;
  private Long category_id;
  private Long user_id;
}
