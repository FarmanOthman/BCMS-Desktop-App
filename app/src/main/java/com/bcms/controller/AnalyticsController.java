package com.bcms.controller;

import com.bcms.viewmodel.AnalyticsViewModel;
import javafx.fxml.FXML;

public class AnalyticsController extends BaseController {
    
    private AnalyticsViewModel viewModel;
    
    @Override
    protected String getCurrentPageName() {
        return "analytics";
    }
    
    @Override
    protected void initializeController() {
        viewModel = new AnalyticsViewModel();
        System.out.println("Analytics page initialized");
    }
}