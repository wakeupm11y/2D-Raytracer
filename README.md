# Java Ray Tracer

A 2D interactive ray tracer built with Java Swing. Rays are cast from a configurable point light source and tested analytically against circular geometry objects in the scene. Objects and the light source can be repositioned in real time by clicking and dragging.

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java) ![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue) ![License](https://img.shields.io/badge/license-MIT-green)

---

## Overview

The simulation places a yellow point light source on a black canvas. It emits `N` rays in all directions (evenly spaced across 360°). Each ray is tested against every target circle in the scene using analytic ray–circle intersection math. Rays that miss all targets are extended to the nearest panel boundary; rays that hit a target stop at the surface. The result is a real-time shadow/occlusion effect rendered purely in 2D.

---

## Features

- **Analytic ray–circle intersection** — no step-based approximation; intersections are solved exactly via the quadratic formula
- **Real-time interaction** — drag the light source or any target circle; rays are recomputed instantly on each mouse move event
- **Configurable ray count** — set `Frame.NUM_RAYS` to trade off fidelity vs. performance
- **Multi-object scenes** — add any number of target circles via `DrawPanel.addShapes()`
- **Boundary clamping** — rays that miss all geometry are correctly terminated at the panel edge, with proper handling of axis-aligned and diagonal directions
- **Swing rendering** — anti-aliased, double-buffered painting via `paintComponent` / `Graphics2D`

---

## Project Structure

```
src/
├── Main.java                  # Entry point — launches GUI on the EDT
├── gui/
│   ├── Frame.java             # JFrame setup; scene configuration
│   └── DrawPanel.java         # JPanel; rendering + mouse interaction
├── objects/
│   ├── Geometry.java          # Abstract base class for scene objects
│   ├── Circle.java            # Concrete circle geometry
│   └── LightSource.java       # Emitter: owns a Circle + Ray array
└── raytracer/
    ├── Ray.java               # Ray data: origin, direction, step, endpoints
    └── RayTracer.java         # Intersection solver (quadratic formula)
```

---

## Techniques & Algorithms

### Ray–Circle Intersection (Analytic)

Each ray is expressed parametrically as **P(t) = O + t·D**, where **O** is the origin and **D** is the unit direction. Substituting into the circle equation **|P − C|² = r²** yields the quadratic:

```
at² + bt + c = 0

a = D·D
b = 2(D·(O − C))
c = (O − C)·(O − C) − r²
```

The discriminant `b² − 4ac` is evaluated:
- **< 0** → miss
- **≥ 0** → solve for both roots `t₁`, `t₂`; take the smallest non-negative value

This is implemented in `RayTracer.solveSteps()`.

### Boundary Projection

Rays that miss all geometry are extended to the nearest panel edge by computing:

```
t_x = (MAX_X − start_x) / x_dir   (or  −start_x / x_dir  for leftward rays)
t_y = (MAX_Y − start_y) / y_dir   (or  −start_y / y_dir  for upward rays)
step = min(t_x, t_y)
```

### Ray Generation

`LightSource.generateRays()` distributes `numRays` rays evenly around the full circle:

```java
double angle = (2π / numRays) * i;
direction = (cos(angle), sin(angle));
```

---

## Getting Started

### Prerequisites

- Java 17 or later (uses pattern matching for `instanceof`)
- Any standard Java IDE (IntelliJ IDEA, Eclipse, VS Code + Extension Pack for Java)

### Running

1. Clone the repository:
```bash
   git clone https://github.com/your-username/java-ray-tracer.git
   cd java-ray-tracer
```

2. Compile (from root):
```bash
   make
```

3. Run:
```bash
   make run
```

Or open the project in your IDE and run `Main.java` directly.

---

## ⚙️ Configuration

| Constant | Location | Default | Description |
|---|---|---|---|
| `NUM_RAYS` | `Frame.java` | `300` | Number of rays emitted by the light source |
| `WIDTH` | `Frame.java` | `1000` | Panel width in pixels |
| `HEIGHT` | `Frame.java` | `600` | Panel height in pixels |

To add more objects to the scene, edit the `Frame` constructor:

```java
drawPanel.addShapes(new Circle(x, y, radius, Color.WHITE));
drawPanel.addSource(new LightSource(new Circle(x, y, radius, Color.YELLOW, true), NUM_RAYS));
```

---

## 🛠️ Technologies

| Technology | Role |
|---|---|
| **Java 17+** | Core language (uses sealed pattern matching `instanceof`) |
| **Java Swing** | GUI framework (`JFrame`, `JPanel`, `Graphics2D`) |
| **Java AWT** | Rendering (`Color`, `BasicStroke`, `Ellipse2D`, `RenderingHints`) |
| **AWT Event Model** | Mouse interaction (`MouseListener`, `MouseMotionListener`) |

No external libraries or build tools are required.

---

## ⚠️ Known Limitations

- **Single-target accuracy**: `RayTracer.solveSteps` handles multiple targets, but when a miss is detected early, `solveTillBoundary` is called inside the inner loop — a later hit on another target can still set `t_min` correctly, though the early boundary call is redundant. For correctness with many targets, the boundary fallback should only run after the full target loop.
- **Circles only**: The `Geometry` base class is designed to support other shapes, but only `Circle` is currently implemented.
- **No reflections or refractions**: Rays terminate on first intersection; multi-bounce light transport is not modelled.

---

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.