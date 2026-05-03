import { C, IMG, bg, footer, kicker, metric, screenshot, text, title } from "./common.mjs";

export async function slide04(presentation, ctx) {
  const slide = presentation.slides.add();
  bg(slide, ctx);
  kicker(slide, ctx, "Producto");
  title(slide, ctx, "La demo tiene pantallas reales y un flujo manual para generar reportes.");
  await screenshot(slide, ctx, IMG.home, 70, 184, 518, 292, "Home del sistema");
  await screenshot(slide, ctx, IMG.login, 664, 184, 460, 258, "Login de FoodTrack");
  metric(slide, ctx, "1 click", "Administrador dispara reporte desde historial de ventas", 86, 560, 300, C.blue);
  metric(slide, ctx, "60 s", "Toast flotante se puede cerrar o desaparece solo", 442, 560, 300, C.green);
  metric(slide, ctx, "N admins", "Envio a todos los correos admin reales", 798, 560, 300, C.amber);
  footer(slide, ctx, 4, "Capturas generadas desde http://localhost:4200/home y /login");
  return slide;
}
