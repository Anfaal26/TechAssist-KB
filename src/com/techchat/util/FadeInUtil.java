package com.techchat.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;
import java.util.List;

/**
 * Utility class for applying fade-in animations to JavaFX nodes.
 * Provides various methods for smooth entrance animations.
 */
public class FadeInUtil {

    // Default animation values
    private static final double DEFAULT_DURATION = 600.0;
    private static final double DEFAULT_DELAY = 0.0;
    private static final double DEFAULT_DELAY_INCREMENT = 100.0;
    private static final double DEFAULT_SLIDE_DISTANCE = 20.0;

    /**
     * Fade in a single node from opacity 0 to 1
     * 
     * @param node     The node to animate
     * @param delay    Delay before animation starts (milliseconds)
     * @param duration Duration of the animation (milliseconds)
     */
    public static void fadeIn(Node node, double delay, double duration) {
        if (node == null)
            return;

        node.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delay));
        fade.play();
    }

    /**
     * Fade in a node with default duration
     * 
     * @param node  The node to animate
     * @param delay Delay before animation starts (milliseconds)
     */
    public static void fadeIn(Node node, double delay) {
        fadeIn(node, delay, DEFAULT_DURATION);
    }

    /**
     * Fade in a node with no delay
     * 
     * @param node The node to animate
     */
    public static void fadeIn(Node node) {
        fadeIn(node, DEFAULT_DELAY, DEFAULT_DURATION);
    }

    /**
     * Fade in multiple nodes with staggered timing
     * 
     * @param nodes          List of nodes to animate
     * @param delayIncrement Time increment between each node's animation
     *                       (milliseconds)
     * @param duration       Duration of each animation (milliseconds)
     */
    public static void fadeInSequence(List<Node> nodes, double delayIncrement, double duration) {
        if (nodes == null || nodes.isEmpty())
            return;

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node != null) {
                fadeIn(node, i * delayIncrement, duration);
            }
        }
    }

    /**
     * Fade in multiple nodes with default settings
     * 
     * @param nodes List of nodes to animate
     */
    public static void fadeInSequence(List<Node> nodes) {
        fadeInSequence(nodes, DEFAULT_DELAY_INCREMENT, DEFAULT_DURATION);
    }

    /**
     * Fade in a node with a slide-up effect
     * 
     * @param node     The node to animate
     * @param delay    Delay before animation starts (milliseconds)
     * @param duration Duration of the animation (milliseconds)
     */
    public static void fadeInWithSlideUp(Node node, double delay, double duration) {
        if (node == null)
            return;

        node.setOpacity(0);
        node.setTranslateY(DEFAULT_SLIDE_DISTANCE);

        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(duration), node);
        slide.setFromY(DEFAULT_SLIDE_DISTANCE);
        slide.setToY(0);

        ParallelTransition parallel = new ParallelTransition(fade, slide);
        parallel.setDelay(Duration.millis(delay));
        parallel.play();
    }

    /**
     * Fade in a node with slide-up effect and default duration
     * 
     * @param node  The node to animate
     * @param delay Delay before animation starts (milliseconds)
     */
    public static void fadeInWithSlideUp(Node node, double delay) {
        fadeInWithSlideUp(node, delay, DEFAULT_DURATION);
    }

    /**
     * Fade in a node with a scale effect (growing from 90% to 100%)
     * 
     * @param node     The node to animate
     * @param delay    Delay before animation starts (milliseconds)
     * @param duration Duration of the animation (milliseconds)
     */
    public static void fadeInWithScale(Node node, double delay, double duration) {
        if (node == null)
            return;

        node.setOpacity(0);
        node.setScaleX(0.9);
        node.setScaleY(0.9);

        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(duration), node);
        scale.setFromX(0.9);
        scale.setFromY(0.9);
        scale.setToX(1.0);
        scale.setToY(1.0);

        ParallelTransition parallel = new ParallelTransition(fade, scale);
        parallel.setDelay(Duration.millis(delay));
        parallel.play();
    }

    /**
     * Fade in a node with scale effect and default duration
     * 
     * @param node  The node to animate
     * @param delay Delay before animation starts (milliseconds)
     */
    public static void fadeInWithScale(Node node, double delay) {
        fadeInWithScale(node, delay, DEFAULT_DURATION);
    }

    /**
     * Automatically fade in all children of a parent node with staggered timing
     * 
     * @param parent         The parent node whose children should be animated
     * @param startDelay     Initial delay before first child animation
     *                       (milliseconds)
     * @param delayIncrement Time increment between each child's animation
     *                       (milliseconds)
     */
    public static void fadeInChildren(Parent parent, double startDelay, double delayIncrement) {
        if (parent == null)
            return;

        List<Node> children = parent.getChildrenUnmodifiable();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if (child != null) {
                fadeIn(child, startDelay + (i * delayIncrement), DEFAULT_DURATION);
            }
        }
    }

    /**
     * Fade in all children with default settings
     * 
     * @param parent The parent node whose children should be animated
     */
    public static void fadeInChildren(Parent parent) {
        fadeInChildren(parent, DEFAULT_DELAY, DEFAULT_DELAY_INCREMENT);
    }
}
