export const C = {
  ink: "#101827",
  paper: "#F7F5EF",
  white: "#FFFFFF",
  soft: "#687386",
  muted: "#ECE7DD",
  line: "#D9D3C7",
  green: "#2E7D5B",
  green2: "#BFE4D1",
  amber: "#D58A24",
  amber2: "#F4D19D",
  blue: "#2563EB",
  blue2: "#CFE0FF",
  red: "#B42318",
  red2: "#F2C8C4",
  dark2: "#172033",
};

export const IMG = {
  home: "C:/Users/jacob/Desktop/Spring/presentation-assets/home.png",
  login: "C:/Users/jacob/Desktop/Spring/presentation-assets/login.png",
};

export function text(slide, ctx, value, x, y, w, h, opts = {}) {
  return ctx.addText(slide, {
    text: String(value ?? ""),
    left: x,
    top: y,
    width: w,
    height: h,
    fontSize: opts.size ?? 18,
    color: opts.color ?? C.ink,
    bold: Boolean(opts.bold),
    typeface: opts.face ?? (opts.mono ? ctx.fonts.mono : opts.title ? ctx.fonts.title : ctx.fonts.body),
    align: opts.align ?? "left",
    valign: opts.valign ?? "top",
    fill: opts.fill ?? "#00000000",
    line: opts.line ?? ctx.line(),
    insets: opts.insets ?? { left: 0, right: 0, top: 0, bottom: 0 },
    name: opts.name,
  });
}

export function rect(slide, ctx, x, y, w, h, fill, opts = {}) {
  return ctx.addShape(slide, {
    left: x,
    top: y,
    width: w,
    height: h,
    geometry: opts.geometry ?? "rect",
    fill,
    line: opts.line ?? ctx.line(opts.stroke ?? "#00000000", opts.weight ?? 0),
    name: opts.name,
  });
}

export function rule(slide, ctx, x, y, w, color = C.line, weight = 1) {
  return rect(slide, ctx, x, y, w, weight, color);
}

export function bg(slide, ctx, dark = false) {
  rect(slide, ctx, 0, 0, ctx.W, ctx.H, dark ? C.ink : C.paper);
}

export function kicker(slide, ctx, label, dark = false) {
  rect(slide, ctx, 58, 50, 10, 10, dark ? C.amber : C.green);
  text(slide, ctx, label.toUpperCase(), 80, 44, 520, 24, {
    size: 10,
    bold: true,
    color: dark ? "#DDE6F3" : C.soft,
  });
}

export function title(slide, ctx, value, dark = false, x = 58, y = 84, w = 860, h = 96, size = 34) {
  text(slide, ctx, value, x, y, w, h, {
    size,
    bold: true,
    title: true,
    color: dark ? C.white : C.ink,
  });
}

export function footer(slide, ctx, page, source, dark = false) {
  rule(slide, ctx, 58, 676, 1164, dark ? "#2A3449" : C.line, 1);
  text(slide, ctx, source, 58, 688, 900, 18, {
    size: 8,
    color: dark ? "#AAB6C8" : C.soft,
  });
  text(slide, ctx, String(page).padStart(2, "0"), 1180, 684, 42, 20, {
    size: 12,
    bold: true,
    align: "right",
    title: true,
    color: dark ? C.white : C.ink,
  });
}

export function chip(slide, ctx, value, x, y, w, fill, color = C.ink) {
  rect(slide, ctx, x, y, w, 28, fill, { line: ctx.line(fill, 0) });
  text(slide, ctx, value, x + 12, y + 7, w - 24, 14, { size: 9, bold: true, color, valign: "middle" });
}

export function node(slide, ctx, label, note, x, y, w, h, fill, stroke = C.line) {
  rect(slide, ctx, x, y, w, h, fill, { line: ctx.line(stroke, 1) });
  text(slide, ctx, label, x + 14, y + 12, w - 28, 24, { size: 14, bold: true, color: C.ink });
  text(slide, ctx, note, x + 14, y + 40, w - 28, h - 58, { size: 10.2, color: C.soft });
}

export function metric(slide, ctx, value, label, x, y, w, color = C.green, dark = false) {
  rule(slide, ctx, x, y, 1, color, 56);
  text(slide, ctx, value, x + 14, y - 4, w - 14, 32, { size: 25, bold: true, title: true, color: dark ? C.white : C.ink });
  text(slide, ctx, label, x + 14, y + 34, w - 14, 26, { size: 9.5, bold: true, color: dark ? "#B6C1D4" : C.soft });
}

export function bullet(slide, ctx, value, x, y, w, color = C.green, dark = false) {
  rect(slide, ctx, x, y + 7, 7, 7, color);
  text(slide, ctx, value, x + 18, y, w - 18, 34, { size: 11, color: dark ? "#DDE6F3" : C.ink });
}

export function code(slide, ctx, value, x, y, w, h, dark = false) {
  rect(slide, ctx, x, y, w, h, dark ? "#0B1020" : "#FFFFFF", { line: ctx.line(dark ? "#29344C" : C.line, 1) });
  text(slide, ctx, value, x + 16, y + 14, w - 32, h - 28, {
    size: 9.2,
    mono: true,
    color: dark ? "#DDE6F3" : C.ink,
  });
}

export function arrow(slide, ctx, x1, y1, x2, y2, color = C.soft) {
  if (Math.abs(y2 - y1) < 2) {
    rule(slide, ctx, x1, y1, x2 - x1, color, 2);
  } else {
    rect(slide, ctx, x1, Math.min(y1, y2), 2, Math.abs(y2 - y1), color);
  }
}

export async function screenshot(slide, ctx, path, x, y, w, h, label) {
  rect(slide, ctx, x - 4, y - 4, w + 8, h + 8, C.white, { line: ctx.line("#C8C1B6", 1) });
  await ctx.addImage(slide, { path, left: x, top: y, width: w, height: h, fit: "cover", alt: label });
  text(slide, ctx, label, x, y + h + 10, w, 18, { size: 9, color: C.soft, bold: true });
}
