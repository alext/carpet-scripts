__config() -> {
   'commands' -> {
     'circle <radius> <block>' -> 'circle',
     'parabolic <center> <radius> <height> <block>' -> 'parabolic',
     'catenary <center> <radius> <height> <block>' -> ['catenary', 1],
     'catenary <center> <radius> <height> <block> <factor>' -> 'catenary',
   },
   'arguments' -> {
     'center' -> {'type' -> 'pos'},
     'radius' -> {'type' -> 'int', 'min' -> 1, 'suggest' -> [10, 32]},
     'height' -> {'type' -> 'int', 'min' -> 1, 'suggest' -> [5, 20]},
     'block' -> {'type' -> 'block'},
     'factor' -> {'type' -> 'float', 'suggest' -> [1, 1.7]},
   }
};

circle(radius, blk) -> (
  center_pos = pos(player());
  print('center ' + center_pos);

  radSq = radius ^ 2;

  c_for(x=0, x<radius, x+=1,
    c_for(z=0, z<radius, z+=1,
      r = x^2 + z^2;
      if(r <= radSq,
        _setQuadrants(center_pos, [x, 0, z], blk)
      )
    )
  );
);

parabolic(center, radius, height, blk) -> (
  heightScale = height / radius^2;

  c_for(x=0, x<=radius, x+=1,
    c_for(z=0, z<=radius, z+=1,
      y = height - (x^2 + z^2) * heightScale;
      if(y >= 0,
        _setQuadrants(center, [x,y,z], blk)
      )
    )
  );
);

_cat(x, z, factor) -> (
  factor * cosh(sqrt(x^2 + z^2) / factor) - factor;
);

catenary(center, radius, height, blk, factor) -> (
  heightScale = height / _cat(radius, 0, factor);

  c_for(x=0, x<=radius, x+=1,
    c_for(z=0, z<=radius, z+=1,
      y = height - _cat(x, z, factor) * heightScale;
      if(y >= 0,
        _setQuadrants(center, [x,y,z], blk)
      )
    )
  );
);

_setQuadrants(center, offset, blk) -> (
  for([1,-1],
    xmod = _;
    for([1, -1],
      zmod = _;
      set(center:0 + offset:0*xmod, center:1 + offset:1, center:2 + offset:2*zmod, blk)
    )
  )
);
