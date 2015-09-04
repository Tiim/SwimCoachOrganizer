package ch.tiim.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.event.EventDispatchChain;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MockParent extends Parent {
    @Override
    protected ObservableList<Node> getChildren() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<Node> getChildrenUnmodifiable() {
        return FXCollections.emptyObservableList();
    }

    @Override
    protected <E extends Node> List<E> getManagedChildren() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public Node lookup(String selector) {
        return null;
    }

    @Override
    public void requestLayout() {
    }

    @Override
    public double prefWidth(double height) {
        return 0;
    }

    @Override
    public double prefHeight(double width) {
        return 0;
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    protected double computePrefWidth(double height) {
        return 0;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 0;
    }

    @Override
    protected double computeMinWidth(double height) {
        return 0;
    }

    @Override
    protected double computeMinHeight(double width) {
        return 0;
    }

    @Override
    public double getBaselineOffset() {
        return 0;
    }

    @Override
    protected void layoutChildren() {
    }

    public MockParent() {
        super();
    }


    @Override
    protected void updateBounds() {
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        return null;
    }

    @Override
    public boolean hasProperties() {
        return false;
    }

    @Override
    public void setUserData(Object value) {
    }

    @Override
    public Object getUserData() {
        return null;
    }

    @Override
    public Set<Node> lookupAll(String selector) {
        return Collections.emptySet();
    }

    @Override
    public void toBack() {
    }

    @Override
    public void toFront() {
    }

    @Override
    public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
        return null;
    }

    @Override
    public void snapshot(Callback<SnapshotResult, Void> callback, SnapshotParameters params, WritableImage image) {
    }

    @Override
    public Dragboard startDragAndDrop(TransferMode... transferModes) {
        return null;
    }

    @Override
    public void startFullDrag() {
    }

    @Override
    public void relocate(double x, double y) {
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public Orientation getContentBias() {
        return null;
    }

    @Override
    public double maxWidth(double height) {
        return 0;
    }

    @Override
    public double maxHeight(double width) {
        return 0;
    }

    @Override
    public void resize(double width, double height) {

    }

    @Override
    public void resizeRelocate(double x, double y, double width, double height) {

    }

    @Override
    public double computeAreaInScreen() {
        return 0;
    }

    @Override
    public boolean contains(double localX, double localY) {
        return false;
    }

    @Override
    public boolean contains(Point2D localPoint) {
        return false;
    }

    @Override
    public boolean intersects(double localX, double localY, double localWidth, double localHeight) {
        return false;
    }

    @Override
    public boolean intersects(Bounds localBounds) {
        return false;
    }

    @Override
    public Point2D screenToLocal(double screenX, double screenY) {
        return null;
    }

    @Override
    public Point2D screenToLocal(Point2D screenPoint) {
        return null;
    }

    @Override
    public Bounds screenToLocal(Bounds screenBounds) {
        return null;
    }

    @Override
    public Point2D sceneToLocal(double x, double y, boolean rootScene) {
        return null;
    }

    @Override
    public Point2D sceneToLocal(Point2D point, boolean rootScene) {
        return null;
    }

    @Override
    public Bounds sceneToLocal(Bounds bounds, boolean rootScene) {
        return null;
    }

    @Override
    public Point2D sceneToLocal(double sceneX, double sceneY) {
        return null;
    }

    @Override
    public Point2D sceneToLocal(Point2D scenePoint) {
        return null;
    }

    @Override
    public Point3D sceneToLocal(Point3D scenePoint) {
        return null;
    }

    @Override
    public Point3D sceneToLocal(double sceneX, double sceneY, double sceneZ) {
        return null;
    }

    @Override
    public Bounds sceneToLocal(Bounds sceneBounds) {
        return null;
    }

    @Override
    public Point2D localToScreen(double localX, double localY) {
        return null;
    }

    @Override
    public Point2D localToScreen(Point2D localPoint) {
        return null;
    }

    @Override
    public Point2D localToScreen(double localX, double localY, double localZ) {
        return null;
    }

    @Override
    public Point2D localToScreen(Point3D localPoint) {
        return null;
    }

    @Override
    public Bounds localToScreen(Bounds localBounds) {
        return null;
    }

    @Override
    public Point2D localToScene(double localX, double localY) {
        return null;
    }

    @Override
    public Point2D localToScene(Point2D localPoint) {
        return null;
    }

    @Override
    public Point3D localToScene(Point3D localPoint) {
        return null;
    }

    @Override
    public Point3D localToScene(double x, double y, double z) {
        return null;
    }

    @Override
    public Point3D localToScene(Point3D localPoint, boolean rootScene) {
        return null;
    }

    @Override
    public Point3D localToScene(double x, double y, double z, boolean rootScene) {
        return null;
    }

    @Override
    public Point2D localToScene(Point2D localPoint, boolean rootScene) {
        return null;
    }

    @Override
    public Point2D localToScene(double x, double y, boolean rootScene) {
        return null;
    }

    @Override
    public Bounds localToScene(Bounds localBounds, boolean rootScene) {
        return null;
    }

    @Override
    public Bounds localToScene(Bounds localBounds) {
        return null;
    }

    @Override
    public Point2D parentToLocal(double parentX, double parentY) {
        return null;
    }

    @Override
    public Point2D parentToLocal(Point2D parentPoint) {
        return null;
    }

    @Override
    public Point3D parentToLocal(Point3D parentPoint) {
        return null;
    }

    @Override
    public Point3D parentToLocal(double parentX, double parentY, double parentZ) {
        return null;
    }

    @Override
    public Bounds parentToLocal(Bounds parentBounds) {
        return null;
    }

    @Override
    public Point2D localToParent(double localX, double localY) {
        return null;
    }

    @Override
    public Point2D localToParent(Point2D localPoint) {
        return null;
    }

    @Override
    public Point3D localToParent(Point3D localPoint) {
        return null;
    }

    @Override
    public Point3D localToParent(double x, double y, double z) {
        return null;
    }

    @Override
    public Bounds localToParent(Bounds localBounds) {
        return null;
    }

    @Override
    public boolean usesMirroring() {
        return false;
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return null;
    }

    @Override
    public String getTypeSelector() {
        return null;
    }

    @Override
    public Styleable getStyleableParent() {
        return null;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return null;
    }

    @Override
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
    }
}
