import { C, arrow, bg, footer, kicker, node, text, title } from "./common.mjs";

export async function slide10(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "Pipeline CI/CD");
  title(slide, ctx, "El pipeline valida backend, frontend y empaquetado antes de construir imagenes.");
  node(slide, ctx, "Push / PR", "GitHub Actions dispara workflows", 70, 286, 150, 92, C.white, C.ink);
  node(slide, ctx, "Backend tests", "JDK 17, Maven cache, test, surefire reports", 286, 224, 188, 112, "#E9F7EF", C.green);
  node(slide, ctx, "Frontend tests", "Node 20, npm ci, ChromeHeadless", 286, 390, 188, 112, "#EAF4FF", C.blue);
  node(slide, ctx, "Build artifacts", "JAR y dist Angular como artefactos", 560, 286, 188, 102, C.white, C.amber);
  node(slide, ctx, "Docker images", "Build de imagen backend y frontend", 834, 286, 188, 102, "#FFF0D5", C.amber);
  node(slide, ctx, "Quality gate", "Resumen de pruebas y estado", 1088, 286, 132, 102, "#F8E2DF", C.red);
  arrow(slide, ctx, 220, 332, 286, 280, C.soft);
  arrow(slide, ctx, 220, 332, 286, 446, C.soft);
  arrow(slide, ctx, 474, 280, 560, 332, C.green);
  arrow(slide, ctx, 474, 446, 560, 332, C.blue);
  arrow(slide, ctx, 748, 334, 834, 334, C.amber);
  arrow(slide, ctx, 1022, 334, 1088, 334, C.red);
  text(slide, ctx, ".github/workflows/ci.yml + ci-backend.yml", 72, 548, 430, 22, { size: 13, mono: true, bold: true, color: C.ink });
  text(slide, ctx, "Esto da evidencia repetible: no depende de ejecutar la demo manualmente para saber si el proyecto compila y prueba.", 72, 578, 720, 42, { size: 13, color: C.soft });
  footer(slide, ctx, 10, "CI/CD: Maven, npm, ChromeHeadless, artifacts, Docker image build");
  return slide;
}
