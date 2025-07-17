package com.bcms.controller;

import com.bcms.viewmodel.FinanceViewModel;
import javafx.fxml.FXML;

public class FinanceController extends BaseController {
    
    private FinanceViewModel viewModel;
    
    @Override
    protected String getCurrentPageName() {
        return "finance";
    }
    
    @Override
    protected void initializeController() {
        viewModel = new FinanceViewModel();
        System.out.println("Finance page initialized");
    }
}