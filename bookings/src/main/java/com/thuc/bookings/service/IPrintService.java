package com.thuc.bookings.service;

import java.io.FileNotFoundException;

public interface IPrintService {
    byte[] exportBill(Integer id) throws FileNotFoundException;
}
