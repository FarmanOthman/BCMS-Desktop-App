package com.bcms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Helper class for integrating sidebar component into other controllers
 */
public class SidebarHelper {
    
    /**
     * Loads the sidebar component and returns the VBox node
     */
    public static VBox loadSidebar() throws IOException {
        FXMLLoader loader = new FXMLLoader(SidebarHelper.class.getResource("/fxml/Sidebar.fxml"));
        return loader.load();
    }
    
    /**
     * Loads the sidebar component and returns both the VBox node and the controller
     */
    public static SidebarComponents loadSidebarWithController() throws IOException {
        FXMLLoader loader = new FXMLLoader(SidebarHelper.class.getResource("/fxml/Sidebar.fxml"));
        VBox sidebarNode = loader.load();
        SidebarController controller = loader.getController();
        return new SidebarComponents(sidebarNode, controller);
    }
    
    /**
     * Inner class to hold both the sidebar node and controller
     */
    public static class SidebarComponents {
        private final VBox sidebarNode;
        private final SidebarController controller;
        
        public SidebarComponents(VBox sidebarNode, SidebarController controller) {
            this.sidebarNode = sidebarNode;
            this.controller = controller;
        }
        
        public VBox getSidebarNode() { return sidebarNode; }
        public SidebarController getController() { return controller; }
    }
}
