import { C, arrow, bg, footer, kicker, node, text, title } from "./common.mjs";

export async function slide02(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "Arquitectura");
  title(slide, ctx, "La API de reportes es externa, pero comparte la misma fuente de verdad.");
  node(slide, ctx, "Angular SPA", "UI operativa, historial de ventas, boton manual de reporte", 72, 240, 190, 108, C.white, C.green);
  node(slide, ctx, "Backend FoodTrack", "Spring Boot 3.4.4, REST, casos de uso, seguridad, JPA", 358, 208, 230, 138, C.white, C.green);
  node(slide, ctx, "MySQL foodtrack", "orders, order_items, products, app_users", 700, 232, 198, 112, C.white, C.amber);
  node(slide, ctx, "ReportService", "Spring Boot externo: datos, OpenAI, email, scheduler", 358, 430, 230, 130, C.blue2, C.blue);
  node(slide, ctx, "OpenAI API", "Generacion del reporte profesional; fallback si falla", 702, 420, 196, 86, C.white, C.blue);
  node(slide, ctx, "Gmail SMTP", "Entrega a todos los administradores con email real", 960, 420, 196, 86, C.white, C.amber);
  arrow(slide, ctx, 262, 294, 358, 294, C.green);
  text(slide, ctx, "REST 8080", 282, 270, 70, 18, { size: 9, color: C.green, bold: true });
  arrow(slide, ctx, 588, 278, 700, 278, C.amber);
  text(slide, ctx, "JPA", 634, 254, 44, 18, { size: 9, color: C.amber, bold: true });
  arrow(slide, ctx, 473, 346, 473, 430, C.blue);
  text(slide, ctx, "POST /api/admin/reports/generate-now", 500, 366, 230, 18, { size: 9, color: C.blue, bold: true });
  arrow(slide, ctx, 588, 492, 702, 464, C.blue);
  arrow(slide, ctx, 898, 464, 960, 464, C.amber);
  arrow(slide, ctx, 588, 492, 700, 310, C.amber);
  text(slide, ctx, "misma BD", 628, 390, 74, 18, { size: 9, color: C.amber, bold: true });
  footer(slide, ctx, 2, "Fuentes: docker-compose.yml, application.properties, ReportService application.yml");
  return slide;
}
