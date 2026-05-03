import { C, IMG, bg, footer, metric, screenshot, text } from "./common.mjs";

export async function slide01(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx, true);
  text(slide, ctx, "FOODTRACK", 58, 52, 220, 24, { size: 12, bold: true, color: C.green2 });
  text(slide, ctx, "Presentacion tecnica del proyecto", 58, 82, 380, 22, { size: 11, color: "#AAB6C8" });
  text(slide, ctx, "Gestion de restaurante con reportes IA externos", 58, 158, 680, 146, {
    size: 47,
    bold: true,
    title: true,
    color: C.white,
  });
  text(slide, ctx, "Arquitectura Spring Boot + Angular + MySQL, integrada con una API externa que genera reportes diarios con OpenAI y envia correo a administradores.", 60, 330, 590, 68, {
    size: 16,
    color: "#DDE6F3",
  });
  metric(slide, ctx, "8080", "Backend principal", 64, 482, 150, C.green, true);
  metric(slide, ctx, "8081", "ReportService externo", 260, 482, 180, C.blue, true);
  metric(slide, ctx, "20:00", "Scheduler diario", 496, 482, 170, C.amber, true);
  await screenshot(slide, ctx, IMG.home, 760, 95, 410, 230, "Frontend FoodTrack - home");
  await screenshot(slide, ctx, IMG.login, 820, 372, 350, 196, "Pantalla de acceso");
  footer(slide, ctx, 1, "FoodTrack | evidencia: capturas locales del frontend y servicios 8080/8081", true);
  return slide;
}
