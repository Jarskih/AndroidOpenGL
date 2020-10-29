precision mediump float;
uniform vec4 color;
uniform sampler2D texture;

varying vec2 vTexCoordinate;

void main() {
    gl_FragColor = color * texture2D(u_Texture, v_TexCoordinate).rgba;
}