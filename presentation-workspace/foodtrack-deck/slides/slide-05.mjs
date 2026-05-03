import { C, arrow, bg, footer, kicker, node, text, title } from "./common.mjs";

export async function slide05(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx, true);
  kicker(slide, ctx, "API externa", true);
  title(slide, ctx, "ReportService orquesta datos reales, IA, fallback y correo.", true);
  const y = 292;
  node(slide, ctx, "1. Request", "Manual: POST /api/reports/generate-and-send\nAutomatico: @Scheduled 20:00", 58, y, 205, 124, "#EAF4FF", C.blue);
  node(slide, ctx, "2. SalesDataService", "Consulta MySQL foodtrack por fecha y filtra admins validos", 316, y, 205, 124, "#E9F7EF", C.green);
  node(slide, ctx, "3. OpenAIService", "Genera reporte ejecutivo; si falla, produce reporte basico", 574, y, 205, 124, "#EAF4FF", C.blue);
  node(slide, ctx, "4. EmailService", "SMTP Gmail envia el reporte a cada administrador", 832, y, 205, 124, "#FFF0D5", C.amber);
  node(slide, ctx, "5. Resultado", "JSON de estado y logs con exito/fallo por destinatario", 1082, y, 140, 124, "#F8E2DF", C.red);
  arrow(slide, ctx, 263, y + 62, 316, y + 62, C.white);
  arrow(slide, ctx, 521, y + 62, 574, y + 62, C.white);
  arrow(slide, ctx, 779, y + 62, 832, y + 62, C.white);
  arrow(slide, ctx, 1037, y + 62, 1082, y + 62, C.white);
  text(slide, ctx, "@Scheduled(cron=\"${reports.scheduler.cron:0 0 20 * * *}\", zone=\"America/Bogota\")", 138, 505, 865, 28, {
    size: 12,
    mono: true,
    color: "#DDE6F3",
  });
  text(slide, ctx, "Decisiones clave: configuracion por variables de entorno, ddl-auto validate, open-in-view false, errores SMTP detallados.", 138, 548, 900, 44, {
    size: 14,
    color: "#DDE6F3",
  });
  footer(slide, ctx, 5, "ReportController, ReportProcessingService, SalesDataService, OpenAIService, EmailService", true);
  return slide;
}
