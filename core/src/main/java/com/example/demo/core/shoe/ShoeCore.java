package com.example.demo.core.shoe;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeToStock;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Shoes;

public interface ShoeCore {

  Shoes search(ShoeFilter filter);

}
