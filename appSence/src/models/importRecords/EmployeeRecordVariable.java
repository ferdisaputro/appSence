/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.importRecords;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EmployeeRecordVariable {
    private int myVariable;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setMyVariable(int newValue) {
        int oldValue = this.myVariable;
        this.myVariable = newValue;
        support.firePropertyChange("myVariable", oldValue, newValue);
    }

    public int getMyVariable() {
        return myVariable;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}